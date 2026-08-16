// See https://svelte.dev/docs/kit/types#app.d.ts

import type { Breadcrumb } from '$lib/layout/routes';

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
			selfScrolling?: boolean;
		}
		// interface PageState {}
		// interface Platform {}
	}
}

export {};
