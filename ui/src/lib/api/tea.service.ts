import { api, unwrap } from './client';
import type { TeaDTO } from './gen/types';

function create(tea: TeaDTO) {
	return unwrap(api.POST('/api/teas', { body: tea }));
}

function getAll() {
	return unwrap(api.GET('/api/teas'));
}

function getById(id: number) {
	return unwrap(api.GET('/api/teas/{id}', { params: { path: { id: id } } }));
}

function update(tea: TeaDTO) {
	return unwrap(
		api.PUT('/api/teas/{id}', {
			body: tea,
			params: { path: { id: tea.id! } }
		})
	);
}

function remove(id: number) {
	return api.DELETE('/api/teas/{id}', { params: { path: { id: id } } });
}

export const teaService = {
	create,
	getAll,
	getById,
	update,
	delete: remove
};
