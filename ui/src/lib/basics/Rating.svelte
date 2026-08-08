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
		name="rating-10"
		class="rating-hidden"
		aria-label="clear"
		{disabled}
		onclick={() => (value = 0)}
		type="radio"
	/>
	{#each { length: 5 }, i}
		<input
			name="rating-{uid}"
			class="mask mask-star"
			aria-label="{i + 1} star"
			checked={i === value - 1}
			{disabled}
			onclick={() => (value = i + 1)}
			type="radio"
		/>
	{/each}
</div>
