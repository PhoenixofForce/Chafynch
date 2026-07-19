<script lang="ts">
	import type { HTMLTextareaAttributes } from 'svelte/elements';

	let {
		value = $bindable(),
		placeholder = 'Placeholder',
		validity,
		hint,
		class: className,
		textareaClass,
		...rest
	}: {
		value?: string;
		placeholder?: string;
		validity?: string;
		hint?: string;
		textareaClass?: string;
	} & HTMLTextareaAttributes = $props();
</script>

<div>
	<label class="floating-label flex-col {className}">
		<span>{placeholder}</span>

		<div class="validator">
			<textarea
				class="textarea {textareaClass}"
				{placeholder}
				bind:value
				{...rest}
				{@attach (node) => node.setCustomValidity(validity ?? '')}
			></textarea>
		</div>
		<p class="validator-hint mt-0 mb-0">{validity ?? hint}</p>
	</label>
</div>
