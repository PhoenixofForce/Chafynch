import { api, mutate, unwrap } from './client';
import type { CultivarDto } from './gen/types';

function create(cultivar: string) {
	return mutate(api.POST('/api/cultivars', { params: { query: { name: cultivar } } }));
}

function getAll(query?: string): Promise<CultivarDto[]> {
	return unwrap(api.GET('/api/cultivars', { params: { query: { q: query } } }));
}

function update(cultivar: CultivarDto) {
	return mutate(
		api.PUT('/api/cultivars/{id}', {
			body: cultivar,
			params: { path: { id: cultivar.id! } }
		})
	);
}

function remove(id: number) {
	return mutate(api.DELETE('/api/cultivars/{id}', { params: { path: { id: id } } }));
}

export const cultivarService = {
	create,
	getAll,
	update,
	delete: remove
};
