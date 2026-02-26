import React, { useEffect, useMemo } from 'react';
import { View, Text, FlatList, StyleSheet, Switch, TouchableOpacity, ActivityIndicator, RefreshControl, Image } from 'react-native';
import { useTaskStore } from '../state/useTaskStore';
import { useRealm, useQuery, TaskSchema } from '../../data/local/TaskSchema';
import { TaskRepositoryImpl } from '../../data/repositories/TaskRepositoryImpl';
import { Task } from '../../domain/entities/Task';
import { AvatarView } from '../components/AvatarView';
import { useNavigation } from '@react-navigation/native';
import { StackNavigationProp } from '@react-navigation/stack';

type RootStackParamList = {
    Dashboard: undefined;
    TaskDetail: { task: Task };
};

export const DashboardScreen = () => {
    const navigation = useNavigation<StackNavigationProp<RootStackParamList>>();
    const realm = useRealm();
    const repository = useMemo(() => new TaskRepositoryImpl(realm), [realm]);

    const { filter, setFilter, tasks, setTasks, isLoading, syncFromApi } = useTaskStore();

    // Read from Realm
    const realmTasks = useQuery('Task');

    useEffect(() => {
        if (realmTasks.length === 0) {
            handleRefresh();
        }
    }, []);

    useEffect(() => {
        const mappedTasks = (realmTasks as unknown as TaskSchema[]).map(t => ({
            id: t.id,
            todo: t.todo,
            completed: t.completed,
            userId: t.userId,
            photoPath: t.photoPath,
        } as Task));
        setTasks(mappedTasks);
    }, [realmTasks]);

    const filteredTasks = useMemo(() => {
        if (filter === 'All') return tasks;
        if (filter === 'Completed') return tasks.filter(t => t.completed);
        return tasks.filter(t => !t.completed);
    }, [tasks, filter]);

    const handleToggleTask = (task: Task) => {
        repository.updateTask({ ...task, completed: !task.completed });
    };

    const handleRefresh = async () => {
        try {
            const apiTasks = await syncFromApi();
            await repository.syncTasks(apiTasks);
        } catch (error) {
            console.error('Sync failed', error);
        }
    };

    const renderItem = ({ item }: { item: Task }) => (
        <TouchableOpacity
            onPress={() => navigation.navigate('TaskDetail', { task: item })}
            style={styles.taskCard}
        >
            <View style={styles.cardMain}>
                <AvatarView name={`U${item.userId}`} style={styles.avatar} />
                <View style={styles.textContainer}>
                    <Text style={[styles.taskTitle, item.completed && styles.completedText]} numberOfLines={2}>
                        {item.todo}
                    </Text>
                    <Text style={styles.userTag}>User #{item.userId}</Text>
                    {item.photoPath && (
                        <View style={styles.photoIndicator}>
                            <Text style={styles.photoIndicatorText}>📸 Foto adjunta</Text>
                        </View>
                    )}
                </View>
                <View style={styles.switchWrapper}>
                    <Switch
                        value={item.completed}
                        onValueChange={() => handleToggleTask(item)}
                        trackColor={{ false: '#3E3E3E', true: '#E94560' }}
                        thumbColor={item.completed ? '#FFF' : '#F4F3F4'}
                    />
                </View>
            </View>
        </TouchableOpacity>
    );

    return (
        <View style={styles.container}>
            <View style={styles.header}>
                <View>
                    <Text style={styles.headerTitle}>TaskNexus</Text>
                    <Text style={styles.headerSubtitle}>Tus tareas, conectadas.</Text>
                </View>
                <TouchableOpacity onPress={handleRefresh} style={styles.syncButton}>
                    {isLoading ? <ActivityIndicator color="#E94560" /> : <Text style={styles.syncButtonText}>Sincronizar</Text>}
                </TouchableOpacity>
            </View>

            <View style={styles.filterContainer}>
                {(['All', 'Completed', 'Pending'] as const).map(f => (
                    <TouchableOpacity
                        key={f}
                        onPress={() => setFilter(f)}
                        style={[styles.filterButton, filter === f && styles.activeFilter]}
                    >
                        <Text style={[styles.filterText, filter === f && styles.activeFilterText]}>
                            {f === 'All' ? 'Todas' : f === 'Completed' ? 'Listas' : 'Pendientes'}
                        </Text>
                    </TouchableOpacity>
                ))}
            </View>

            <FlatList
                data={filteredTasks}
                renderItem={renderItem}
                keyExtractor={item => item.id.toString()}
                contentContainerStyle={styles.list}
                showsVerticalScrollIndicator={false}
                refreshControl={
                    <RefreshControl refreshing={isLoading} onRefresh={handleRefresh} tintColor="#E94560" />
                }
                ListEmptyComponent={
                    !isLoading ? <Text style={styles.empty}>No se encontraron tareas</Text> : null
                }
            />
        </View>
    );
};

const styles = StyleSheet.create({
    container: { flex: 1, backgroundColor: '#1A1A2E' },
    header: {
        paddingHorizontal: 25,
        paddingTop: 60,
        paddingBottom: 25,
        backgroundColor: '#16213E',
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        borderBottomRightRadius: 40
    },
    headerTitle: { color: '#E94560', fontSize: 32, fontWeight: 'bold', letterSpacing: 1 },
    headerSubtitle: { color: '#999', fontSize: 14, marginTop: 2 },
    syncButton: { backgroundColor: '#0F3460', paddingVertical: 8, paddingHorizontal: 15, borderRadius: 20, borderWidth: 1, borderColor: '#1A1A2E' },
    syncButtonText: { color: '#E94560', fontWeight: 'bold' },
    filterContainer: { flexDirection: 'row', padding: 15, backgroundColor: 'transparent', marginVertical: 10 },
    filterButton: { paddingVertical: 10, paddingHorizontal: 20, borderRadius: 25, marginRight: 10, backgroundColor: '#16213E' },
    activeFilter: { backgroundColor: '#E94560' },
    filterText: { color: '#999', fontWeight: 'bold' },
    activeFilterText: { color: '#FFF' },
    list: { padding: 20, paddingBottom: 100 },
    taskCard: {
        backgroundColor: '#16213E',
        borderRadius: 20,
        marginBottom: 15,
        padding: 18,
        elevation: 10,
        shadowColor: '#000',
        shadowOffset: { width: 0, height: 10 },
        shadowOpacity: 0.3,
        shadowRadius: 15,
    },
    cardMain: { flexDirection: 'row', alignItems: 'center' },
    avatar: { width: 50, height: 50, borderRadius: 25, marginRight: 15 },
    textContainer: { flex: 1 },
    taskTitle: { fontSize: 17, color: '#FFF', fontWeight: '600', marginBottom: 4 },
    completedText: { textDecorationLine: 'line-through', color: '#533483' },
    userTag: { fontSize: 12, color: '#E94560', fontWeight: 'bold', textTransform: 'uppercase' },
    photoIndicator: { marginTop: 8 },
    photoIndicatorText: { color: '#4CAF50', fontSize: 12, fontWeight: 'bold' },
    switchWrapper: { paddingLeft: 10 },
    empty: { textAlign: 'center', marginTop: 100, color: '#999', fontSize: 16 },
});
