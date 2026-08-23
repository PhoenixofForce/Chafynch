import { api, unwrap } from './client';
import type { ExtractionProfile, ExtractionResult } from './gen/types';

function getAll(): Promise<ExtractionProfile[]> {
	return unwrap(api.GET('/api/extract/all'));
}

function create(extractionProfile: ExtractionProfile) {
	return unwrap(api.POST('/api/extract', { body: extractionProfile }));
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
	extract,
	extractWithProfile
};
