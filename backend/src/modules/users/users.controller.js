import { usersService } from './users.service.js'

export const usersController = {

    async getById(request, reply) {
        const user = await usersService.getById(request.params.id)
        return reply.status(200).send({ data: user })
    },

    async getByProject(request, reply) {
        const users = await usersService.getByProject(request.params.projectId)
        return reply.status(200).send({ data: users })
    },

    async update(request, reply) {
        // Users can only update themselves unless they are ADMIN
        const requesterId = request.user.id
        const targetId = request.params.id

        if (requesterId !== targetId && request.user.role !== 'ADMIN') {
            return reply.status(403).send({ error: 'No tienes permiso para editar este usuario', code: 403 })
        }

        const user = await usersService.update(targetId, request.body)
        return reply.status(200).send({ data: user })
    }
}
