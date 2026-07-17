<script lang="ts">
	import type { Snippet } from 'svelte';
	import type { EventHandler, HTMLAttributes } from 'svelte/elements';

	let {
		open = $bindable(false),
		class: className,
		actions,
		children,
		onclose,
		...rest
	}: {
		open: boolean;
		actions?: Snippet;
		children?: Snippet;
		onclose?: EventHandler<Event, HTMLDialogElement>;
	} & HTMLAttributes<HTMLDialogElement> = $props();
	let modal: HTMLDialogElement;

	$effect(() => {
		if (open) modal?.showModal();
		else modal?.close();
	});
</script>

<dialog
	class="modal {className}"
	{...rest}
	bind:this={modal}
	onclose={(e) => {
		open = false;
		onclose?.(e);
	}}
>
	<div class="modal-box">
		{#if open}
			{@render children?.()}
			{#if actions}
				<div class="modal-action">
					<form method="dialog">
						{@render actions()}
					</form>
				</div>
			{/if}
		{/if}
	</div>
	<form method="dialog" class="modal-backdrop">
		<button>close</button>
	</form>
</dialog>
