import { useTaskStore } from '../../presentation/state/useTaskStore';
import { Task } from '../../domain/entities/Task';

describe('useTaskStore', () => {
    beforeEach(() => {
        // Reset store state before each test
        useTaskStore.setState({
            tasks: [],
            isLoading: false,
            filter: 'All',
        });
    });

    it('should have initial state', () => {
        const state = useTaskStore.getState();
        expect(state.tasks).toEqual([]);
        expect(state.isLoading).toBe(false);
        expect(state.filter).toBe('All');
    });

    it('should set tasks correctly', () => {
        const mockTasks: Task[] = [
            { id: 1, todo: 'Task 1', completed: false, userId: 1 },
            { id: 2, todo: 'Task 2', completed: true, userId: 1 },
        ];

        useTaskStore.getState().setTasks(mockTasks);
        expect(useTaskStore.getState().tasks).toEqual(mockTasks);
    });

    it('should update filter correctly', () => {
        useTaskStore.getState().setFilter('Completed');
        expect(useTaskStore.getState().filter).toBe('Completed');

        useTaskStore.getState().setFilter('Pending');
        expect(useTaskStore.getState().filter).toBe('Pending');
    });
});
