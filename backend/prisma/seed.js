import { PrismaClient } from '@prisma/client'
import bcrypt from 'bcryptjs'

const prisma = new PrismaClient()

async function main() {
    console.log('🌱 Seeding database...')

    // ── Users ─────────────────────────────────────────────────
    const adminPass = await bcrypt.hash('Admin1234!', 12)
    const memberPass = await bcrypt.hash('Member1234!', 12)

    const admin = await prisma.user.upsert({
        where: { email: 'admin@taskdashboard.com' },
        update: {},
        create: { name: 'Admin User', email: 'admin@taskdashboard.com', password: adminPass, role: 'ADMIN' }
    })

    const alice = await prisma.user.upsert({
        where: { email: 'alice@taskdashboard.com' },
        update: {},
        create: { name: 'Alice García', email: 'alice@taskdashboard.com', password: memberPass, role: 'MEMBER' }
    })

    const bob = await prisma.user.upsert({
        where: { email: 'bob@taskdashboard.com' },
        update: {},
        create: { name: 'Bob Martínez', email: 'bob@taskdashboard.com', password: memberPass, role: 'MEMBER' }
    })

    // ── Project ───────────────────────────────────────────────
    const project = await prisma.project.upsert({
        where: { id: 'seed-project-001' },
        update: {},
        create: { id: 'seed-project-001', name: 'Dashboard MVP', description: 'Proyecto principal del equipo' }
    })

    // ── Project Members ───────────────────────────────────────
    for (const userId of [admin.id, alice.id, bob.id]) {
        await prisma.projectMember.upsert({
            where: { userId_projectId: { userId, projectId: project.id } },
            update: {},
            create: { userId, projectId: project.id }
        })
    }

    // ── Tasks ─────────────────────────────────────────────────
    const tasks = [
        { title: 'Configurar entorno de desarrollo', status: 'DONE', priority: 'HIGH', assignedUserId: admin.id },
        { title: 'Diseñar pantallas de la app', status: 'IN_PROGRESS', priority: 'HIGH', assignedUserId: alice.id },
        { title: 'Implementar autenticación JWT', status: 'IN_PROGRESS', priority: 'HIGH', assignedUserId: admin.id },
        { title: 'Crear endpoint de tareas', status: 'DONE', priority: 'MEDIUM', assignedUserId: bob.id },
        { title: 'Escribir pruebas unitarias', status: 'TODO', priority: 'MEDIUM', assignedUserId: alice.id },
        { title: 'Configurar CI/CD', status: 'TODO', priority: 'LOW', assignedUserId: bob.id },
        { title: 'Documentar API con Swagger', status: 'TODO', priority: 'LOW', assignedUserId: admin.id },
        { title: 'Revisar pull requests del sprint', status: 'TODO', priority: 'MEDIUM', assignedUserId: alice.id },
    ]

    for (const task of tasks) {
        await prisma.task.create({
            data: { ...task, projectId: project.id, dueDate: new Date(Date.now() + 7 * 24 * 60 * 60 * 1000) }
        })
    }

    console.log('✅ Seed completed!')
    console.log('   Admin:  admin@taskdashboard.com  / Admin1234!')
    console.log('   Alice:  alice@taskdashboard.com  / Member1234!')
    console.log('   Bob:    bob@taskdashboard.com    / Member1234!')
}

main()
    .catch(e => { console.error(e); process.exit(1) })
    .finally(async () => prisma.$disconnect())
