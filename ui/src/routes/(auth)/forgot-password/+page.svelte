<script lang="ts">
	import Button from '$lib/basics/Button.svelte';
	import Input from '$lib/basics/Input.svelte';
	import { RotateCcw } from '@lucide/svelte';
	import AuthCard from '../AuthCard.svelte';
</script>

<AuthCard title="Reset Password" backPath="/login" backTitle="Back to login">
	<div class="w-full">
		<Input
			name="username"
			autocomplete="username"
			placeholder="Username or E-Mail*"
			required
			hint="Username is required"
			inputClass="w-full"
		/>
	</div>

	<div class="w-full">
		<Button type="submit" class="w-full btn-primary" label="Reset Password" icon={RotateCcw} />
	</div>
</AuthCard>
