import { prisma } from '../../config/database.js'

export const tasksService = {

    async getAll(filters = {}) {
        return prisma.task.findMany({
            where: filters,
            orderBy: { createdAt: 'desc' },
            include: {
                assignedUser: { select: { id: true, name: true, email: true, avatarUrl: true } },
                project: { select: { id: true, name: true } }
            }
        })
    },

    async getById(id) {
        const task = await prisma.task.findUnique({
            where: { id },
            include: {
                assignedUser: { select: { id: true, name: true, email: true, avatarUrl: true } },
                project: { select: { id: true, name: true } }
            }
        })
        if (!task) throw { statusCode: 404, message: 'Tarea no encontrada' }
        return task
    },

    async getByUser(userId) {
        return prisma.task.findMany({
            where: { assignedUserId: userId },
            orderBy: { createdAt: 'desc' },
            include: { project: { select: { id: true, name: true } } }
        })
    },

    async create(data) {
        return prisma.task.create({
            data: {
                id: data.id,
                title: data.title,
                description: data.description ?? null,
                status: data.status ?? 'TODO',
                priority: data.priority ?? 'MEDIUM',
                assignedUserId: data.assigned_user_id ?? null,
                projectId: data.project_id ?? null,
                dueDate: data.due_date ? new Date(data.due_date) : null
            }
        })
    },

    async update(id, data) {
        await tasksService.getById(id) // throws 404 if not found
        return prisma.task.update({
            where: { id },
            data: {
                title: data.title,
                description: data.description,
                status: data.status,
                priority: data.priority,
                assignedUserId: data.assigned_user_id,
                projectId: data.project_id,
                dueDate: data.due_date ? new Date(data.due_date) : undefined
            }
        })
    },

    async remove(id) {
        await tasksService.getById(id) // throws 404 if not found
        await prisma.task.delete({ where: { id } })
    }
}
