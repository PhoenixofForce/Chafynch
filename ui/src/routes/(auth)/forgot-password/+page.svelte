<script lang="ts">
	import Button from '$lib/components/ui/Button.svelte';
	import Input from '$lib/components/ui/Input.svelte';
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
		<Button type="submit" class="w-full btn-primary" label="Reset Password" />
	</div>
</AuthCard>
