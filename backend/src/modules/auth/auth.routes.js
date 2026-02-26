import { authController } from './auth.controller.js'

/**
 * Auth routes – public (no JWT required)
 * POST /v1/auth/login
 * POST /v1/auth/register
 * POST /v1/auth/logout
 * POST /v1/auth/refresh
 * GET  /v1/auth/me
 */
export async function authRoutes(fastify) {
    fastify.post('/login', authController.login)
    fastify.post('/register', authController.register)
    fastify.post('/logout', { preHandler: [fastify.authenticate] }, authController.logout)
    fastify.post('/refresh', authController.refresh)
    fastify.get('/me', { preHandler: [fastify.authenticate] }, authController.me)
}
