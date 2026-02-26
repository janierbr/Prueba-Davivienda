import { TaskRepositoryImpl } from '../../data/repositories/TaskRepositoryImpl';
import { Task } from '../../domain/entities/Task';

// Mock simple de Realm
const mockRealm: any = {
    objects: jest.fn(),
    write: jest.fn((callback) => callback()),
    create: jest.fn(),
};

describe('TaskRepositoryImpl', () => {
    let repository: TaskRepositoryImpl;

    beforeEach(() => {
        jest.clearAllMocks();
        repository = new TaskRepositoryImpl(mockRealm);
    });

    it('should get tasks from realm and map them to domain entities', async () => {
        const mockRealmTasks = [
            { id: 1, todo: 'Test task', completed: false, userId: 1 },
        ];
        mockRealm.objects.mockReturnValue(mockRealmTasks);

        const tasks = await repository.getTasks();

        expect(mockRealm.objects).toHaveBeenCalledWith('Task');
        expect(tasks).toEqual([
            { id: 1, todo: 'Test task', completed: false, userId: 1 },
        ]);
    });

    it('should update task in realm', async () => {
        const taskToUpdate: Task = { id: 1, todo: 'Updated', completed: true, userId: 1 };

        await repository.updateTask(taskToUpdate);

        expect(mockRealm.write).toHaveBeenCalled();
        expect(mockRealm.create).toHaveBeenCalledWith(
            'Task',
            { id: 1, completed: true },
            'modified'
        );
    });

    it('should sync multiple tasks to realm', async () => {
        const tasksToSync: Task[] = [
            { id: 1, todo: 'Task 1', completed: false, userId: 1 },
            { id: 2, todo: 'Task 2', completed: true, userId: 1 },
        ];

        await repository.syncTasks(tasksToSync);

        expect(mockRealm.write).toHaveBeenCalled();
        expect(mockRealm.create).toHaveBeenCalledTimes(2);
    });
});
