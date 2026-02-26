import axios from 'axios';
import { Task } from '../../domain/entities/Task';

const API_URL = 'https://dummyjson.com/todos';

export const TaskApiService = {
    fetchTasks: async (): Promise<Task[]> => {
        const response = await axios.get<{ todos: Task[] }>(API_URL);
        return response.data.todos;
    },
};
