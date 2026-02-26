import { tasksService } from './tasks.service.js'

export const tasksController = {

    async getAll(request, reply) {
        // Optional query filters: ?status=TODO&priority=HIGH&projectId=xxx
        const { status, priority, projectId } = request.query
        const filters = {}
        if (status) filters.status = status
        if (priority) filters.priority = priority
        if (projectId) filters.projectId = projectId

        const tasks = await tasksService.getAll(filters)
        return reply.status(200).send({ data: tasks })
    },

    async getById(request, reply) {
        const task = await tasksService.getById(request.params.id)
        return reply.status(200).send({ data: task })
    },

    async getByUser(request, reply) {
        const tasks = await tasksService.getByUser(request.params.userId)
        return reply.status(200).send({ data: tasks })
    },

    async create(request, reply) {
        const { title } = request.body
        if (!title) {
            return reply.status(400).send({ error: 'El título es requerido', code: 400 })
        }
        const task = await tasksService.create(request.body)
        return reply.status(201).send({ data: task })
    },

    async update(request, reply) {
        const task = await tasksService.update(request.params.id, request.body)
        return reply.status(200).send({ data: task })
    },

    async remove(request, reply) {
        await tasksService.remove(request.params.id)
        return reply.status(204).send()
    }
}
