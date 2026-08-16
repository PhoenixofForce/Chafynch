import { sessionService } from '$lib/api/session.service.js';
import { type Breadcrumb } from '$lib/layout/routes';

export async function load({ params, parent }) {
	const { breadcrumbs, tea } = await parent();
	const slug = parseInt(params.slug);
	const sessionId = parseInt(params.sessionId);
	const session = await sessionService.getById(sessionId);

	return {
		tea,
		session,
		selfScrolling: true,
		breadcrumbs: [
			...breadcrumbs,
			{ label: 'Session ' + sessionId, path: `/teas/${slug}/session/${sessionId}` }
		] satisfies Breadcrumb[]
	};
}
