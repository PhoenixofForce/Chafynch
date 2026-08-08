<script lang="ts">
	import { onMount, type ComponentProps } from 'svelte';
	import Input from './Input.svelte';

	let {
		placeholder = '',
		value = $bindable(),
		options,
		search,
		strict = false,
		validity,

		class: className,
		...rest
	}: {
		placeholder?: string;
		value?: string;
		options: string[];
		search?: (q: string) => Promise<string[]>;
		strict?: boolean;
		validity?: string;
	} & Omit<
		ComponentProps<typeof Input>,
		'validity' | 'value' | 'oninput' | 'onblur' | 'onfocus'
	> = $props();

	let open = $state(false);
	let debounceTimer: ReturnType<typeof setTimeout>;

	let suggestions = $state<string[]>([]);
	let valid = $derived(!strict || !value || suggestions.includes(value));

	function onInput(e: Event) {
		const query = (e.target as HTMLInputElement).value;
		value = query;
		open = true;

		fetchSuggestions();
	}

	function select(s: string) {
		value = s;
		open = false;
		fetchSuggestions();
	}

	function onFocus() {
		open = true;
		fetchSuggestions();
	}

	function fetchSuggestions() {
		clearTimeout(debounceTimer);

		if (!search) {
			suggestions = options.filter((o) => !value || o.toLowerCase().includes(value.toLowerCase()));
			return;
		}

		debounceTimer = setTimeout(async () => {
			suggestions = (await search?.(value ?? '')) ?? [];
		}, 200);
	}

	onMount(() => {
		fetchSuggestions();
	});
</script>

<div class="relative {className}">
	<Input
		autocomplete="off"
		onblur={() => (open = false)}
		onfocus={onFocus}
		oninput={onInput}
		{placeholder}
		validity={validity ?? (!valid ? 'Choose an object from the list' : '')}
		{value}
		{...rest}
		inputClass="w-full {rest.inputClass ?? ''}"
	/>
	{#if open}
		<ul
			class="menu absolute z-50 mt-1 h-fit max-h-48 w-full flex-nowrap overflow-x-hidden overflow-y-auto rounded-lg border border-base-300 bg-base-100 shadow"
		>
			{#if suggestions.length === 0}
				<li>{value ? 'No Entries' : 'Start typing'}</li>
			{/if}

			{#each suggestions as option (option)}
				<li>
					<button
						class="hover:bg-primary"
						onmousedown={(e) => {
							e.preventDefault();
							select(option);
						}}
						type="button"
					>
						{option}
					</button>
				</li>
			{/each}
		</ul>
	{/if}
</div>
