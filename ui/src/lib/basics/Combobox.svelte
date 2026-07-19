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
		if (!value) {
			suggestions = options;
			return;
		}
		const query = value;

		if (!search) {
			suggestions = options.filter((o) => o.toLowerCase().includes(query.toLowerCase()));
			return;
		}

		debounceTimer = setTimeout(async () => {
			suggestions = (await search?.(query)) ?? [];
		}, 200);
	}

	onMount(() => {
		fetchSuggestions();
	});
</script>

<div class="relative {className}">
	<Input
		validity={validity ?? (!valid ? 'Choose an object from the list' : '')}
		{placeholder}
		{value}
		oninput={onInput}
		onfocus={onFocus}
		onblur={() => (open = false)}
		autocomplete="off"
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
						type="button"
						onmousedown={(e) => {
							e.preventDefault();
							select(option);
						}}
					>
						{option}
					</button>
				</li>
			{/each}
		</ul>
	{/if}
</div>
