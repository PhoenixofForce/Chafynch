import { api, mutate, unwrap } from './client';
import type { TeaTypeDto } from './gen/types';

function create(teaType: TeaTypeDto) {
	return mutate(api.POST('/api/tea-types', { params: { query: { name: teaType.name } } }));
}

function getAll() {
	return unwrap(api.GET('/api/tea-types'));
}

function update(teaType: TeaTypeDto) {
	return mutate(
		api.PUT('/api/tea-types/{id}', {
			body: teaType,
			params: { path: { id: teaType.id! } }
		})
	);
}

function remove(id: number) {
	return mutate(api.DELETE('/api/tea-types/{id}', { params: { path: { id: id } } }));
}

export const teaTypeService = {
	create,
	getAll,
	update,
	delete: remove
};
