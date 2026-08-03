<script lang="ts">
	import type { HTMLAttributes } from 'svelte/elements';

	const uid = $props.id();
	let {
		value = $bindable(0),
		disabled,
		class: className,
		...rest
	}: { value?: number; disabled?: boolean } & HTMLAttributes<HTMLDivElement> = $props();
</script>

<div class="rating {className}" {...rest}>
	<input
		type="radio"
		name="rating-10"
		class="rating-hidden"
		aria-label="clear"
		onclick={() => (value = 0)}
		{disabled}
	/>
	{#each { length: 5 }, i}
		<input
			type="radio"
			name="rating-{uid}"
			class="mask mask-star"
			aria-label="{i + 1} star"
			checked={i === value - 1}
			onclick={() => (value = i + 1)}
			{disabled}
		/>
	{/each}
</div>
