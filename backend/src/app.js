import 'dotenv/config'
import Fastify from 'fastify'
import fastifyJwt from '@fastify/jwt'
import fastifyCors from '@fastify/cors'
import fastifyHelmet from '@fastify/helmet'
import fastifySensible from '@fastify/sensible'

import { authRoutes } from './modules/auth/auth.routes.js'
import { tasksRoutes } from './modules/tasks/tasks.routes.js'
import { usersRoutes } from './modules/users/users.routes.js'
import { authenticate } from './middleware/authenticate.js'

export async function buildApp() {
  const app = Fastify({ logger: true })

  // ── Plugins ───────────────────────────────────────────────
  await app.register(fastifyHelmet)
  await app.register(fastifySensible)
  await app.register(fastifyCors, {
    origin: '*', // Adjust for production
    methods: ['GET', 'POST', 'PUT', 'DELETE']
  })
  await app.register(fastifyJwt, {
    secret: process.env.JWT_SECRET,
    sign: { expiresIn: process.env.JWT_EXPIRES_IN ?? '1h' }
  })

  // ── Decorators ────────────────────────────────────────────
  app.decorate('authenticate', authenticate)

  // ── Health check ──────────────────────────────────────────
  app.get('/health', async () => ({ status: 'ok', timestamp: new Date().toISOString() }))

  // ── Routes ────────────────────────────────────────────────
  await app.register(authRoutes, { prefix: '/v1/auth' })
  await app.register(tasksRoutes, { prefix: '/v1/tasks' })
  await app.register(usersRoutes, { prefix: '/v1/users' })

  // ── Error handler ─────────────────────────────────────────
  app.setErrorHandler((error, request, reply) => {
    app.log.error(error)
    const statusCode = error.statusCode ?? 500
    reply.status(statusCode).send({
      error: error.message ?? 'Internal Server Error',
      code: statusCode
    })
  })

  return app
}
