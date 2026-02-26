/**
 * Fastify preHandler hook for JWT authentication.
 * Usage: { preHandler: [fastify.authenticate] }
 */
export async function authenticate(request, reply) {
    try {
        await request.jwtVerify()
    } catch (err) {
        reply.status(401).send({ error: 'Unauthorized', code: 401 })
    }
}
