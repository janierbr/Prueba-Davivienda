import { Realm } from '@realm/react';
import { Task } from '../../domain/entities/Task';
import { TaskRepository } from '../../domain/repositories/TaskRepository';
import { TaskSchema } from '../local/TaskSchema';

export class TaskRepositoryImpl implements TaskRepository {
    private realm: Realm;

    constructor(realm: Realm) {
        this.realm = realm;
    }

    async getTasks(): Promise<Task[]> {
        const tasks = this.realm.objects<TaskSchema>('Task');
        return tasks.map(t => ({
            id: t.id,
            todo: t.todo,
            completed: t.completed,
            userId: t.userId,
            photoPath: t.photoPath,
        }));
    }

    async updateTask(task: Task): Promise<void> {
        this.realm.write(() => {
            this.realm.create(
                'Task',
                {
                    id: task.id,
                    todo: task.todo,
                    completed: task.completed,
                    photoPath: task.photoPath,
                },
                Realm.UpdateMode.Modified
            );
        });
    }

    async deleteTask(id: number): Promise<void> {
        this.realm.write(() => {
            const task = this.realm.objectForPrimaryKey<TaskSchema>('Task', id);
            if (task) {
                this.realm.delete(task);
            }
        });
    }

    async syncTasks(tasks: Task[]): Promise<void> {
        this.realm.write(() => {
            tasks.forEach(task => {
                this.realm.create('Task', {
                    id: task.id,
                    todo: task.todo,
                    completed: task.completed,
                    userId: task.userId,
                    // keep existing photoPath if sync happens
                }, Realm.UpdateMode.Modified);
            });
        });
    }
}
