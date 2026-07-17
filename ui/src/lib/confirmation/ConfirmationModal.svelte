<script lang="ts">
	import Button from '../basics/Button.svelte';
	import Modal from '../basics/Modal.svelte';
	import { confirmation } from './confirmation.store.svelte';
</script>

<Modal open={confirmation.state !== null} onclose={() => confirmation.hide()}>
	<h3 class="text-lg font-bold">
		{confirmation.state?.title ?? 'Do you really want to perform this action'}
	</h3>
	{#if confirmation.state?.description}
		<p class="py-4">{confirmation.state.description}</p>
	{/if}

	{#snippet actions()}
		<Button
			type="submit"
			label={confirmation.state?.cancel?.label ?? 'Cancel'}
			icon={confirmation.state?.cancel?.icon}
		/>
		{#if confirmation.state?.confirm}
			{@const btn = confirmation.state.confirm}
			<Button
				class={btn.class ?? 'btn-success'}
				label={btn.label}
				icon={btn.icon}
				onclick={btn.onclick}
			/>
		{/if}
	{/snippet}
</Modal>
