export function createEditor<T extends { id: number }>() {
	let draft = $state<T | undefined>(undefined);
	let isNew = $state(false);
	let pending = $state(false);

	async function run(func: () => void | Promise<void>) {
		pending = true;
		try {
			await func();
		} finally {
			pending = false;
		}
	}

	return {
		get draft() {
			return draft;
		},
		get isNew() {
			return isNew;
		},
		get isPending() {
			return pending;
		},
		isEditing: (t: T) => draft?.id === t.id,
		editingAny: () => draft !== undefined,
		edit: (t: T) => {
			draft = structuredClone(t);
			isNew = false;
		},
		create: (blank: T) => {
			draft = blank;
			isNew = true;
		},
		cancel: () => {
			draft = undefined;
			isNew = false;
		},
		save: (persist: (t: T, isNew: boolean) => void | Promise<void>) => {
			return run(async () => {
				if (!draft) return;
				await persist(draft, isNew);
				draft = undefined;
				isNew = false;
			});
		},
		delete: (t: T, persist: (t: T) => void | Promise<void>) => {
			return run(async () => {
				await persist(t);
			});
		}
	};
}
