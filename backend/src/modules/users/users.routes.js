import { usersController } from './users.controller.js'

/**
 * Users routes – all protected (JWT required)
 * GET  /v1/users/:id
 * GET  /v1/users/project/:projectId
 * PUT  /v1/users/:id
 */
export async function usersRoutes(fastify) {
    const auth = { preHandler: [fastify.authenticate] }

    fastify.get('/:id', auth, usersController.getById)
    fastify.get('/project/:projectId', auth, usersController.getByProject)
    fastify.put('/:id', auth, usersController.update)
}
