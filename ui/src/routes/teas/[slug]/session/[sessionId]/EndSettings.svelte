<script lang="ts">
	import type { SessionDto } from '$lib/api/gen/types';
	import Rating from '$lib/basics/Rating.svelte';
	import Textarea from '$lib/basics/Textarea.svelte';

	let { session = $bindable() }: { session: SessionDto } = $props();
</script>

<div class="mb-4 flex flex-col gap-4">
	<div class="w-full text-xs text-base-content/50 uppercase">Summary</div>
	<Rating bind:value={session.rating} />
	<Textarea placeholder="Final notes" textareaClass="w-full" bind:value={session.sessionSummary} />

	<div class="w-full text-xs text-base-content/50 uppercase">For the next brew</div>
	<Textarea
		placeholder="Hint for next Session"
		textareaClass="w-full "
		bind:value={session.nextSessionHint}
	/>
</div>
