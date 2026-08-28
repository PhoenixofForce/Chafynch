import { api, mutate, unwrap } from './client';
import type { ExtractionProfile, ExtractionResult } from './gen/types';

function getAll(): Promise<ExtractionProfile[]> {
	return unwrap(api.GET('/api/extract/all'));
}

function create(extractionProfile: ExtractionProfile) {
	return mutate(api.POST('/api/extract', { body: extractionProfile }));
}

function update(extractionProfile: ExtractionProfile) {
	return mutate(
		api.PUT('/api/extract/{id}', {
			body: extractionProfile,
			params: { path: { id: extractionProfile.id } }
		})
	);
}

function deleteProfile(extractionProfile: ExtractionProfile) {
	return mutate(
		api.DELETE('/api/extract/{id}', {
			params: { path: { id: extractionProfile.id } }
		})
	);
}

function extract(url: string): Promise<ExtractionResult> {
	return unwrap(api.GET('/api/extract', { params: { query: { url } } }));
}

function extractWithProfile(url: string, profile: ExtractionProfile): Promise<ExtractionResult> {
	return unwrap(api.POST('/api/extract/test', { params: { query: { url } }, body: profile }));
}

export const extractionService = {
	getAll,
	create,
	update,
	delete: deleteProfile,
	extract,
	extractWithProfile
};
