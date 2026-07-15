<script lang="ts">
	import Button from '$lib/components/ui/Button.svelte';
	import Input from '$lib/components/ui/Input.svelte';
	import AuthCard from '../AuthCard.svelte';
</script>

<AuthCard title="Register" backPath="/login" backTitle="Already have an account?">
	<div class="w-full">
		<Input
			name="username"
			autocomplete="username"
			placeholder="Username*"
			required
			hint="Username is required"
			inputClass="w-full"
		/>
	</div>

	<div class="w-full">
		<Input
			name="email"
			autocomplete="email"
			placeholder="E-Mail*"
			required
			hint="E-Mail is required"
			inputClass="w-full"
		/>
	</div>

	<div class="w-full">
		<Input
			type="password"
			name="password"
			autocomplete="new-password"
			required
			placeholder="Password*"
			hint="Password is required"
			inputClass="w-full"
		/>
	</div>

	<div class="w-full">
		<Input
			type="password"
			name="confirmedPassword"
			autocomplete="new-password"
			required
			placeholder="Repeat Password*"
			hint="Password is required"
			inputClass="w-full"
		/>
	</div>

	<div class="mt-4 w-full">
		<Button type="submit" class="w-full btn-primary" label="Register" />
	</div>
</AuthCard>
