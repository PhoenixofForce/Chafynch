<script lang="ts">
	// Todo: rework

	type Props = {
		id?: string;
		placeholder?: string;
		value: string;
		search: (q: string) => Promise<string[]>;
	};

	let { id, placeholder = '', value = $bindable(''), search }: Props = $props();

	let suggestions = $state<string[]>([]);
	let open = $state(false);
	let debounceTimer: ReturnType<typeof setTimeout>;

	function onInput(e: Event) {
		const q = (e.target as HTMLInputElement).value;
		value = q;
		clearTimeout(debounceTimer);
		if (q.length < 1) {
			suggestions = [];
			open = false;
			return;
		}
		debounceTimer = setTimeout(async () => {
			suggestions = await search(q);
			open = suggestions.length > 0;
		}, 200);
	}

	function select(s: string) {
		value = s;
		open = false;
	}

	function onBlur() {
		setTimeout(() => (open = false), 150);
	}
</script>

<div class="relative">
	<input
		{id}
		type="text"
		class="input-bordered input w-full"
		{placeholder}
		{value}
		oninput={onInput}
		onblur={onBlur}
		onfocus={() => {
			if (suggestions.length > 0) open = true;
		}}
		autocomplete="off"
	/>
	{#if open}
		<ul
			class="menu absolute z-50 mt-1 max-h-48 w-full overflow-y-auto rounded-lg border border-base-300 bg-base-200 shadow"
		>
			{#each suggestions as s (s)}
				<li>
					<button type="button" onmousedown={() => select(s)}>{s}</button>
				</li>
			{/each}
		</ul>
	{/if}
</div>
