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
		value?: string;
		placeholder?: string;
		validity?: string;
		hint?: string;
		icon?: Component;
		inputClass?: string;
	} & HTMLInputAttributes = $props();
</script>

<div>
	<label class="floating-label {className}">
		<span>{placeholder}</span>

		<div class="validator input {inputClass}">
			<Icon color="currentColor" size="21" />
			<input
				type="text"
				{placeholder}
				bind:value
				{...rest}
				{@attach (node) => node.setCustomValidity(validity ?? '')}
			/>
		</div>
		<p class="validator-hint mt-0 mb-1">{validity ?? hint}</p>
	</label>
</div>
