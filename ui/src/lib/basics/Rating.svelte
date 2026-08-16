<script lang="ts">
	import type { HTMLAttributes } from 'svelte/elements';

	const uid = $props.id();
	let {
		value = $bindable(),
		disabled,
		readonly,
		class: className,
		...rest
	}: {
		value?: number;
		disabled?: boolean;
		readonly?: boolean;
	} & HTMLAttributes<HTMLDivElement> = $props();
</script>

<div class="rating {className}" {...rest}>
	{#if !readonly}
		<input
			name="rating-{uid}"
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
				checked={i === (value ?? 0) - 1}
				{disabled}
				onclick={() => (value = i + 1)}
				type="radio"
			/>
		{/each}
	{:else}
		{#each { length: 5 }, i}
			<div
				class="mask mask-star"
				aria-current={i === (value ?? 0) - 1}
				aria-label="{i + 1} star"
			></div>
		{/each}
	{/if}
</div>
