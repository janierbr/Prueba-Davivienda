import { create } from 'zustand';
import { Task } from '../../domain/entities/Task';
import { TaskApiService } from '../../data/remote/TaskApiService';

interface TaskState {
    tasks: Task[];
    isLoading: boolean;
    filter: 'All' | 'Completed' | 'Pending';
    setFilter: (filter: 'All' | 'Completed' | 'Pending') => void;
    setTasks: (tasks: Task[]) => void;
    syncFromApi: () => Promise<Task[]>;
}

export const useTaskStore = create<TaskState>((set) => ({
    tasks: [],
    isLoading: false,
    filter: 'All',
    setFilter: (filter) => set({ filter }),
    setTasks: (tasks) => set({ tasks }),
    syncFromApi: async () => {
        set({ isLoading: true });
        try {
            const tasks = await TaskApiService.fetchTasks();
            // Note: Persisting to Realm should happen in the component/hook using the repository
            set({ isLoading: false });
            return tasks;
        } catch (error) {
            set({ isLoading: false });
            throw error;
        }
    },
}));
