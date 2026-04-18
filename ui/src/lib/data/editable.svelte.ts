export function createEditor<T extends { id: number }>() {
	let draft = $state<T | undefined>(undefined);
	let isNew = $state(false);

	return {
		get draft() {
			return draft;
		},
		get isNew() {
			return isNew;
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
		save: async (persist: (t: T, isNew: boolean) => void | Promise<void>) => {
			if (!draft) return;
			await persist(draft, isNew);
			draft = undefined;
			isNew = false;
		}
	};
}
