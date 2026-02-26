import { Task } from '../entities/Task';

export interface TaskRepository {
    getTasks(): Promise<Task[]>;
    updateTask(task: Task): Promise<void>;
    syncTasks(tasks: Task[]): Promise<void>;
    deleteTask(id: number): Promise<void>;
}
