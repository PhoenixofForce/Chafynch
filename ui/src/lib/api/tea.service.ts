import { api, mutate, unwrap } from './client';
import type { TeaDTO } from './gen/types';

function create(tea: TeaDTO) {
	return mutate(api.POST('/api/teas', { body: tea }));
}

function getAll() {
	return unwrap(api.GET('/api/teas'));
}

function getById(id: number) {
	return unwrap(api.GET('/api/teas/{id}', { params: { path: { id: id } } }));
}

function update(tea: TeaDTO) {
	return mutate(
		api.PUT('/api/teas/{id}', {
			body: tea,
			params: { path: { id: tea.id! } }
		})
	);
}

function remove(id: number) {
	return mutate(api.DELETE('/api/teas/{id}', { params: { path: { id: id } } }));
}

export const teaService = {
	create,
	getAll,
	getById,
	update,
	delete: remove
};
