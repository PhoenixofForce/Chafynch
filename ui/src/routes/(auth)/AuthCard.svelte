<script lang="ts">
	import type { Snippet } from 'svelte';
	import { resolve } from '$app/paths';
	import type { Pathname } from '$app/types';

	const {
		title,
		backPath,
		backTitle,
		children,
		afterForm
	}: {
		title: string;
		children?: Snippet;
		afterForm?: Snippet;
		backPath: Pathname;
		backTitle: string;
	} = $props();
</script>

<div class="prose mb-8 text-center">
	<h1>Tea Tracker</h1>
</div>

<div class="card w-full max-w-96 bg-base-300 shadow-sm">
	<div class="card-body items-center">
		<h2 class="mb-4 card-title">{title}</h2>
		<form class="flex w-full flex-col gap-2">
			{@render children?.()}
		</form>
		{@render afterForm?.()}
	</div>
</div>
<div class="mt-2 text-sm">
	<a class="link link-neutral" href={resolve(backPath)}> {backTitle} </a>
</div>
