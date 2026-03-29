// See https://svelte.dev/docs/kit/types#app.d.ts

import type { Breadcrumb } from '$lib/routes';

// for information about these interfaces
declare global {
	namespace App {
		interface Error {
			error: string;
			message: string;
			timestamp: string;
			path: string;
			trace: string;
		}
		// interface Locals {}
		interface PageData {
			breadcrumbs: Breadcrumb[];
		}
		// interface PageState {}
		// interface Platform {}
	}
}

export {};
