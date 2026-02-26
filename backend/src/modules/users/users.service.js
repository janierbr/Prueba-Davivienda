import { prisma } from '../../config/database.js'

export const usersService = {

    async getById(id) {
        const user = await prisma.user.findUnique({
            where: { id },
            select: { id: true, name: true, email: true, role: true, avatarUrl: true, createdAt: true }
        })
        if (!user) throw { statusCode: 404, message: 'Usuario no encontrado' }
        return user
    },

    async getByProject(projectId) {
        const members = await prisma.projectMember.findMany({
            where: { projectId },
            include: {
                user: {
                    select: { id: true, name: true, email: true, role: true, avatarUrl: true }
                }
            }
        })
        return members.map(m => m.user)
    },

    async update(id, data) {
        await usersService.getById(id) // throws 404 if not found
        return prisma.user.update({
            where: { id },
            data: {
                name: data.name,
                avatarUrl: data.avatar_url,
                role: data.role
            },
            select: { id: true, name: true, email: true, role: true, avatarUrl: true }
        })
    }
}
