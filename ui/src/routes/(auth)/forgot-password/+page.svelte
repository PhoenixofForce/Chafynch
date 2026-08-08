<script lang="ts">
	import Button from '$lib/basics/Button.svelte';
	import Input from '$lib/basics/Input.svelte';
	import { RotateCcw } from '@lucide/svelte';
	import AuthCard from '../AuthCard.svelte';
</script>

<AuthCard backPath="/login" backTitle="Back to login" title="Reset Password">
	<div class="w-full">
		<Input
			name="username"
			autocomplete="username"
			hint="Username is required"
			inputClass="w-full"
			placeholder="Username or E-Mail*"
			required
		/>
	</div>

	<div class="w-full">
		<Button class="w-full btn-primary" icon={RotateCcw} label="Reset Password" type="submit" />
	</div>
</AuthCard>
