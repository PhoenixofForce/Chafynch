<script lang="ts">
	type Props = {
		id?: string;
		placeholder?: string;
		value: string;
		options: string[];
	};

	let { id, placeholder = '', value = $bindable(''), options }: Props = $props();

	let query = $state(value);
	let open = $state(false);

	let filtered = $derived(
		query.length > 0
			? options.filter((o) => o.toLowerCase().includes(query.toLowerCase()))
			: options
	);

	let valid = $derived(options.includes(value));

	function onInput(e: Event) {
		query = (e.target as HTMLInputElement).value;
		value = query;
		open = true;
	}

	function select(s: string) {
		value = s;
		query = s;
		open = false;
	}

	function onBlur() {
		setTimeout(() => (open = false), 150);
	}

	function onFocus() {
		open = true;
	}
</script>

<div class="relative">
	<input
		{id}
		type="text"
		class="input-bordered input w-full"
		class:input-error={query.length > 0 && !valid}
		{placeholder}
		value={query}
		oninput={onInput}
		onblur={onBlur}
		onfocus={onFocus}
		autocomplete="off"
	/>
	{#if open && filtered.length > 0}
		<ul
			class="menu absolute z-50 mt-1 h-48 w-full flex-nowrap overflow-x-hidden overflow-y-auto rounded-lg border border-base-300 bg-base-200 shadow"
		>
			{#each filtered as s (s)}
				<li>
					<button type="button" onmousedown={() => select(s)}>{s}</button>
				</li>
			{/each}
		</ul>
	{/if}
</div>
