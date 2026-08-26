import { api, mutate, unwrap } from './client';
import type { VendorDto } from './gen/types';

function create(vendor: VendorDto) {
	return mutate(api.POST('/api/vendors', { body: vendor }));
}

function getAll(query?: string) {
	return unwrap(api.GET('/api/vendors', { params: { query: { q: query } } }));
}

function update(vendor: VendorDto) {
	return mutate(
		api.PUT('/api/vendors/{id}', {
			body: vendor,
			params: { path: { id: vendor.id! } }
		})
	);
}

function remove(id: number) {
	return mutate(api.DELETE('/api/vendors/{id}', { params: { path: { id: id } } }));
}

export const vendorService = {
	create,
	getAll,
	update,
	delete: remove
};
