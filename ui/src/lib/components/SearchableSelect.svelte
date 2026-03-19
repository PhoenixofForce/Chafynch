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
		class="input input-bordered w-full"
		class:input-error={query.length > 0 && !valid}
		{placeholder}
		value={query}
		oninput={onInput}
		onblur={onBlur}
		onfocus={onFocus}
		autocomplete="off"
	/>
	{#if open && filtered.length > 0}
		<ul class="menu h-48 w-full flex-nowrap bg-base-200 border-base-300 absolute z-50 mt-1 overflow-y-auto overflow-x-hidden rounded-lg border shadow">
			{#each filtered as s}
				<li>
					<button type="button" onmousedown={() => select(s)}>{s}</button>
				</li>
			{/each}
		</ul>
	{/if}
</div>