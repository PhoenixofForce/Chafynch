import { api, unwrap } from './client';
import type { SessionDto } from './gen/types';

function findForTea(teaId: number): Promise<SessionDto[]> {
	return unwrap(api.GET('/api/sessions/byTea/{teaId}', { params: { path: { teaId } } }));
}

function getById(sessionId: number): Promise<SessionDto> {
	return unwrap(api.GET('/api/sessions/{id}', { params: { path: { id: sessionId } } }));
}

function create(teaId: number, session: SessionDto): Promise<SessionDto> {
	return unwrap(api.POST('/api/sessions/{teaId}', { params: { path: { teaId } }, body: session }));
}

function update(session: SessionDto): Promise<SessionDto> {
	return unwrap(
		api.PUT('/api/sessions/{id}', { body: session, params: { path: { id: session.id! } } })
	);
}

export const sessionService = {
	findForTea,
	getById,
	create,
	update
};
