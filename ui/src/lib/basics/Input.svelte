<script lang="ts">
	import type { Component } from 'svelte';
	import type { HTMLInputAttributes } from 'svelte/elements';

	let {
		value = $bindable(),
		placeholder = 'Placeholder',
		validity,
		hint,
		icon: Icon,
		class: className,
		inputClass,
		...rest
	}: {
		value?: string | number;
		placeholder?: string;
		validity?: string;
		hint?: string;
		icon?: Component;
		inputClass?: string;
	} & HTMLInputAttributes = $props();
</script>

<div>
	<label class="floating-label flex-col {className}">
		<span>{placeholder}</span>

		<div class="validator input {inputClass}">
			<Icon color="currentColor" size="21" />
			<input
				{@attach (node) => node.setCustomValidity(validity ?? '')}
				{placeholder}
				type="text"
				bind:value
				{...rest}
			/>
		</div>
		<p class="validator-hint mt-0 mb-0">{validity ?? hint}</p>
	</label>
</div>
