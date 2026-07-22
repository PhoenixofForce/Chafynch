import { api, unwrap } from './client';
import type { ExtractionProfile, ExtractionResult } from './gen/types';

function create(extractionProfile: ExtractionProfile) {
	return unwrap(api.POST('/api/extract', { body: extractionProfile }));
}

function extract(url: string): Promise<ExtractionResult> {
	return unwrap(api.GET('/api/extract', { params: { query: { url } } }));
}

export const extractionService = {
	create,
	extract
};
