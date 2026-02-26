import { Realm, createRealmContext } from '@realm/react';

export class TaskSchema extends Realm.Object<TaskSchema> {
    id!: number;
    todo!: string;
    completed!: boolean;
    userId!: number;
    photoPath?: string;

    static schema = {
        name: 'Task',
        primaryKey: 'id',
        properties: {
            id: 'int',
            todo: 'string',
            completed: 'bool',
            userId: 'int',
            photoPath: 'string?',
        },
    };
}

export const { RealmProvider, useRealm, useQuery, useObject } = createRealmContext({
    schema: [TaskSchema],
});
