import { tasksController } from './tasks.controller.js'

/**
 * Tasks routes – all protected (JWT required)
 * GET    /v1/tasks
 * GET    /v1/tasks/:id
 * GET    /v1/tasks/user/:userId
 * POST   /v1/tasks
 * PUT    /v1/tasks/:id
 * DELETE /v1/tasks/:id
 */
export async function tasksRoutes(fastify) {
    const auth = { preHandler: [fastify.authenticate] }

    fastify.get('/', auth, tasksController.getAll)
    fastify.get('/user/:userId', auth, tasksController.getByUser)
    fastify.get('/:id', auth, tasksController.getById)
    fastify.post('/', auth, tasksController.create)
    fastify.put('/:id', auth, tasksController.update)
    fastify.delete('/:id', auth, tasksController.remove)
}
