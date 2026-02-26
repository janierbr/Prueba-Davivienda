import bcrypt from 'bcryptjs'
import { v4 as uuidv4 } from 'uuid'
import { prisma } from '../../config/database.js'

export const authService = {

    async login(email, password, fastify) {
        const user = await prisma.user.findUnique({ where: { email } })
        if (!user) throw { statusCode: 401, message: 'Credenciales inválidas' }

        const valid = await bcrypt.compare(password, user.password)
        if (!valid) throw { statusCode: 401, message: 'Credenciales inválidas' }

        const token = fastify.jwt.sign(
            { id: user.id, email: user.email, role: user.role },
            { expiresIn: process.env.JWT_EXPIRES_IN ?? '1h' }
        )

        const refreshToken = uuidv4()
        const expiresAt = new Date(Date.now() + 7 * 24 * 60 * 60 * 1000) // 7 días

        await prisma.refreshToken.create({
            data: { token: refreshToken, userId: user.id, expiresAt }
        })

        return { token, refreshToken, user: sanitizeUser(user) }
    },

    async register(name, email, password) {
        const exists = await prisma.user.findUnique({ where: { email } })
        if (exists) throw { statusCode: 409, message: 'El email ya está registrado' }

        const hashed = await bcrypt.hash(password, 12)
        const user = await prisma.user.create({
            data: { name, email, password: hashed }
        })

        return sanitizeUser(user)
    },

    async logout(userId) {
        await prisma.refreshToken.deleteMany({ where: { userId } })
    },

    async refresh(token, fastify) {
        const stored = await prisma.refreshToken.findUnique({ where: { token } })
        if (!stored || stored.expiresAt < new Date()) {
            throw { statusCode: 401, message: 'Refresh token inválido o expirado' }
        }

        const user = await prisma.user.findUnique({ where: { id: stored.userId } })
        if (!user) throw { statusCode: 404, message: 'Usuario no encontrado' }

        const newToken = fastify.jwt.sign(
            { id: user.id, email: user.email, role: user.role },
            { expiresIn: process.env.JWT_EXPIRES_IN ?? '1h' }
        )

        // Rotar refresh token
        await prisma.refreshToken.delete({ where: { token } })
        const newRefresh = uuidv4()
        const expiresAt = new Date(Date.now() + 7 * 24 * 60 * 60 * 1000)
        await prisma.refreshToken.create({
            data: { token: newRefresh, userId: user.id, expiresAt }
        })

        return { token: newToken, refreshToken: newRefresh }
    },

    async me(userId) {
        const user = await prisma.user.findUnique({ where: { id: userId } })
        if (!user) throw { statusCode: 404, message: 'Usuario no encontrado' }
        return sanitizeUser(user)
    }
}

// Remove password from user response
function sanitizeUser(user) {
    const { password, ...safe } = user
    return safe
}
