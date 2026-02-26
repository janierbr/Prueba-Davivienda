import React, { useState, useMemo } from 'react';
import { View, Text, StyleSheet, TouchableOpacity, TextInput, Image, ScrollView, Alert, Platform, PermissionsAndroid } from 'react-native';
import { useRoute, useNavigation, RouteProp } from '@react-navigation/native';
import { useRealm } from '../../data/local/TaskSchema';
import { TaskRepositoryImpl } from '../../data/repositories/TaskRepositoryImpl';
import { Task } from '../../domain/entities/Task';
import { NativeModules } from 'react-native';

const { CameraModule } = NativeModules;

type ParamList = {
    TaskDetail: { task: Task };
};

export const TaskDetailScreen = () => {
    const route = useRoute<RouteProp<ParamList, 'TaskDetail'>>();
    const navigation = useNavigation();
    const realm = useRealm();
    const repository = useMemo(() => new TaskRepositoryImpl(realm), [realm]);

    const { task } = route.params;
    const [title, setTitle] = useState(task.todo);
    const [photoPath, setPhotoPath] = useState(task.photoPath);
    const [isEditing, setIsEditing] = useState(false);

    const handleSave = async () => {
        try {
            await repository.updateTask({ ...task, todo: title, photoPath });
            // Update local state if needed or just rely on Realm's live collection if we used it, 
            // but here we passed a static task object in params. 
            // In a better architecture we'd use useObject from Realm.
            setIsEditing(false);
            Alert.alert('Success', 'Task updated successfully');
        } catch (error) {
            Alert.alert('Error', 'Failed to update task');
        }
    };

    const handleDelete = () => {
        Alert.alert(
            'Delete Task',
            'Are you sure you want to delete this task?',
            [
                { text: 'Cancel', style: 'cancel' },
                {
                    text: 'Delete',
                    style: 'destructive',
                    onPress: async () => {
                        await repository.deleteTask(task.id);
                        navigation.goBack();
                    }
                },
            ]
        );
    };

    const handleAddPhoto = async () => {
        if (Platform.OS === 'android') {
            const granted = await PermissionsAndroid.request(
                PermissionsAndroid.PERMISSIONS.CAMERA,
                {
                    title: "Camera Permission",
                    message: "App needs access to your camera",
                    buttonNeutral: "Ask Me Later",
                    buttonNegative: "Cancel",
                    buttonPositive: "OK"
                }
            );
            if (granted !== PermissionsAndroid.RESULTS.GRANTED) return;
        }

        try {
            const newPath = await CameraModule.takePhoto();
            setPhotoPath(newPath);
            await repository.updateTask({ ...task, photoPath: newPath });
        } catch (error) {
            console.error('Camera failed', error);
        }
    };

    return (
        <ScrollView style={styles.container}>
            <View style={styles.header}>
                <TouchableOpacity onPress={() => navigation.goBack()} style={styles.backButton}>
                    <Text style={styles.backText}>← Volver</Text>
                </TouchableOpacity>
                <Text style={styles.headerTitle}>Detalles de Tarea</Text>
                <TouchableOpacity onPress={handleDelete} style={styles.deleteButton}>
                    <Text style={styles.deleteText}>Eliminar</Text>
                </TouchableOpacity>
            </View>

            <View style={styles.card}>
                <Text style={styles.label}>Título de la Tarea</Text>
                {isEditing ? (
                    <TextInput
                        style={styles.input}
                        value={title}
                        onChangeText={setTitle}
                        multiline
                    />
                ) : (
                    <Text style={styles.titleText}>{title}</Text>
                )}

                <TouchableOpacity
                    onPress={() => isEditing ? handleSave() : setIsEditing(true)}
                    style={[styles.actionButton, isEditing ? styles.saveButton : styles.editButton]}
                >
                    <Text style={styles.actionButtonText}>
                        {isEditing ? 'Guardar Cambios' : 'Editar Título'}
                    </Text>
                </TouchableOpacity>
            </View>

            <View style={styles.photoCard}>
                <Text style={styles.label}>Fotografía Adjunta</Text>
                {photoPath ? (
                    <Image source={{ uri: photoPath }} style={styles.photo} resizeMode="cover" />
                ) : (
                    <View style={styles.noPhoto}>
                        <Text style={styles.noPhotoText}>No hay foto adjunta</Text>
                    </View>
                )}

                <TouchableOpacity onPress={handleAddPhoto} style={styles.photoActionButton}>
                    <Text style={styles.photoActionButtonText}>
                        {photoPath ? 'Cambiar Fotografía' : 'Tomar Fotografía'}
                    </Text>
                </TouchableOpacity>
            </View>
        </ScrollView>
    );
};

const styles = StyleSheet.create({
    container: { flex: 1, backgroundColor: '#1A1A2E' },
    header: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
        padding: 20,
        paddingTop: 50,
        backgroundColor: '#16213E'
    },
    headerTitle: { color: '#E94560', fontSize: 18, fontWeight: 'bold' },
    backText: { color: '#FFF', fontSize: 16 },
    deleteText: { color: '#FF4D4D', fontSize: 16, fontWeight: 'bold' },
    backButton: { padding: 5 },
    deleteButton: { padding: 5 },
    card: { margin: 20, padding: 20, backgroundColor: '#16213E', borderRadius: 15, elevation: 5 },
    photoCard: { margin: 20, marginTop: 0, padding: 20, backgroundColor: '#16213E', borderRadius: 15, elevation: 5 },
    label: { color: '#999', fontSize: 12, marginBottom: 8, textTransform: 'uppercase' },
    titleText: { color: '#FFF', fontSize: 22, fontWeight: 'bold', marginBottom: 20 },
    input: {
        backgroundColor: '#0F3460',
        color: '#FFF',
        borderRadius: 8,
        padding: 15,
        fontSize: 18,
        marginBottom: 20,
        borderWidth: 1,
        borderColor: '#E94560'
    },
    actionButton: { paddingVertical: 12, borderRadius: 25, alignItems: 'center' },
    editButton: { backgroundColor: '#E94560' },
    saveButton: { backgroundColor: '#4CAF50' },
    actionButtonText: { color: '#FFF', fontWeight: 'bold', fontSize: 16 },
    photo: { width: '100%', height: 300, borderRadius: 10, marginVertical: 15 },
    noPhoto: {
        width: '100%',
        height: 150,
        backgroundColor: '#0F3460',
        borderRadius: 10,
        justifyContent: 'center',
        alignItems: 'center',
        marginVertical: 15
    },
    noPhotoText: { color: '#999' },
    photoActionButton: {
        borderWidth: 1,
        borderColor: '#E94560',
        paddingVertical: 10,
        borderRadius: 25,
        alignItems: 'center'
    },
    photoActionButtonText: { color: '#E94560', fontWeight: 'bold' }
});
