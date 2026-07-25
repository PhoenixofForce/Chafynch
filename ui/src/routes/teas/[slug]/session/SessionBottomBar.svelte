<script lang="ts">
	import Button from '$lib/basics/Button.svelte';
	import { Flag, Leaf, Plus } from '@lucide/svelte';
	import { tick } from 'svelte';

	let infusions = $state(0);
	let activeInfusion = $state(0);
	let scrollable: HTMLDivElement;

	async function addInfusion() {
		infusions++;
		activeInfusion = infusions - 1;
		await tick();
		scrollable.scrollTo({
			left: scrollable.scrollWidth,
			behavior: 'instant'
		});
	}
</script>

<div class="flex w-full gap-6">
	<div class="py-4">
		<Button class="h-20 w-16 btn-ghost" icon={Leaf} />
	</div>
	<div class="flex flex-1 gap-4 overflow-x-auto py-4" bind:this={scrollable}>
		{#each { length: infusions }, i (i)}
			<Button
				class="h-20 w-16 {i == activeInfusion ? 'btn-primary' : 'btn-dash'}"
				onclick={() => (activeInfusion = i)}
			>
				<div class="flex flex-col">
					<b>{i + 1}</b>
					<span class="text-xs text-neutral/80">25s</span>
					<span class="text-xs text-neutral/80">★★★</span>
				</div>
			</Button>
		{/each}
		<Button class="h-20 w-16 btn-dash" icon={Plus} onclick={addInfusion} />
	</div>

	<div class="py-4">
		<Button class="h-20 w-16 btn-ghost" icon={Flag} />
	</div>
</div>
