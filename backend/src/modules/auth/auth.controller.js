import { authService } from './auth.service.js'

export const authController = {

    async login(request, reply) {
        const { email, password } = request.body
        if (!email || !password) {
            return reply.status(400).send({ error: 'Email y contraseña son requeridos', code: 400 })
        }
        const result = await authService.login(email, password, request.server)
        return reply.status(200).send({ data: result })
    },

    async register(request, reply) {
        const { name, email, password } = request.body
        if (!name || !email || !password) {
            return reply.status(400).send({ error: 'Nombre, email y contraseña son requeridos', code: 400 })
        }
        const user = await authService.register(name, email, password)
        return reply.status(201).send({ data: user, message: 'Usuario creado exitosamente' })
    },

    async logout(request, reply) {
        const userId = request.user.id
        await authService.logout(userId)
        return reply.status(204).send()
    },

    async refresh(request, reply) {
        const { token } = request.body
        if (!token) {
            return reply.status(400).send({ error: 'Refresh token requerido', code: 400 })
        }
        const result = await authService.refresh(token, request.server)
        return reply.status(200).send({ data: result })
    },

    async me(request, reply) {
        const user = await authService.me(request.user.id)
        return reply.status(200).send({ data: user })
    }
}
