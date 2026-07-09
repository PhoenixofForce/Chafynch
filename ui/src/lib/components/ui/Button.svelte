<script lang="ts">
	import type { Component } from 'svelte';
	import type { HTMLButtonAttributes } from 'svelte/elements';
	import Loading from './Loading.svelte';

	let {
		label,
		loading,
		icon: Icon,
		class: className,
		disabled,
		...rest
	}: { label?: string; loading?: boolean; icon?: Component } & HTMLButtonAttributes = $props();
</script>

<button
	type="button"
	class="btn {className} transition-transform hover:-translate-y-1 hover:shadow"
	class:btn-square={!label}
	disabled={disabled || loading}
	{...rest}
>
	{#if !loading}
		<Icon />
	{:else}
		<Loading class="loading-sm" />
	{/if}
	{label}
</button>
