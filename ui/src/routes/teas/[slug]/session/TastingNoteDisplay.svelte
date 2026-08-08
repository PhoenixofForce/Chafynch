<script lang="ts">
	import Button from '$lib/basics/Button.svelte';
	import { Plus } from '@lucide/svelte';

	let { name, notes, openModal }: { name: string; notes: string[]; openModal: () => void } =
		$props();
</script>

<div class="flex w-full items-center gap-4 rounded-full bg-base-200 px-4 py-2">
	<span class="min-w-1/10 text-xs text-base-content/80">{name}</span>
	<span class="flex flex-1 flex-wrap gap-2">
		{#each notes as tag (tag)}
			<div class="badge badge-xs badge-accent">{tag}</div>
		{/each}
	</span>
	<Button class="h-4 w-4" icon={Plus} noAnimation onclick={openModal} />
</div>
