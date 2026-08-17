import { api, unwrap } from './client';

function findByNote(query: string, limit: number): Promise<string[]> {
	return unwrap(api.GET('/api/tasting-notes', { params: { query: { query, limit } } }));
}

export const tastingNoteService = {
	findByNote
};
